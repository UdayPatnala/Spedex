#!/bin/sh
set -e

# 1. Memory constraints for Render 512MB free tier container (prevent Exit 137 OOM kills)
JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Xmx384m"

# 2. Database URL normalization for Render PostgreSQL
# Render injects standard postgres URI, but JDBC requires jdbc:postgresql prefix
if [ -n "$DATABASE_URL" ]; then
    case "$DATABASE_URL" in
        postgres://*|postgresql://*)
            JDBC_URL=$(echo "$DATABASE_URL" | sed -e 's|^postgres://|jdbc:postgresql://|' -e 's|^postgresql://|jdbc:postgresql://|')
            export SPRING_DATASOURCE_URL="$JDBC_URL"
            export SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.postgresql.Driver"
            export SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"
            ;;
        jdbc:*)
            export SPRING_DATASOURCE_URL="$DATABASE_URL"
            export SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.postgresql.Driver"
            export SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"
            ;;
    esac
elif [ -z "$SPRING_DATASOURCE_URL" ]; then
    echo "[Render Bootstrap] No external DATABASE_URL detected. Initializing embedded failover H2 database at /tmp/spedex_prod_db..."
    export SPRING_DATASOURCE_URL="jdbc:h2:file:/tmp/spedex_prod_db;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE"
    export SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.h2.Driver"
    export SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.H2Dialect"
fi

# 3. Dynamic JWT Secret fallback for Render if omitted
if [ -z "$JWT_SECRET" ]; then
    export JWT_SECRET="default_prod_jwt_secret_that_is_at_least_256_bits_long_render"
fi

# 4. Dynamic port binding
PORT_PARAM="-Dserver.port=${PORT:-8080}"

echo "[Render Bootstrap] Starting SpeDex Backend on port ${PORT:-8080}..."
exec java $JAVA_OPTS $PORT_PARAM -jar app.jar
