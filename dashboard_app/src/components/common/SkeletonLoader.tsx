import React from 'react';

export interface SkeletonLoaderProps {
  height?: string;
  width?: string;
  borderRadius?: string;
}

export const SkeletonLoader: React.FC<SkeletonLoaderProps> = ({
  height = '20px',
  width = '100%',
  borderRadius = '6px',
}) => {
  return (
    <div
      style={{
        height,
        width,
        borderRadius,
        backgroundColor: 'rgba(255, 255, 255, 0.08)',
        animation: 'pulse 1.5s infinite ease-in-out',
      }}
    />
  );
};
