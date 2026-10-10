package com.spedex.service;

import com.spedex.dto.VendorDto;
import com.spedex.model.User;
import com.spedex.model.Vendor;
import com.spedex.repository.UserRepository;
import com.spedex.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class PaymentReferenceService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) return;
        String trimmed = url.trim().toLowerCase();
        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://") && !trimmed.startsWith("upi://")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment URL must start with http://, https://, or upi://");
        }
    }

    public List<VendorDto> listReferences(String email) {
        User user = getAuthenticatedUser(email);
        List<Vendor> vendors = vendorRepository.findByUserId(user.getId());
        return vendors.stream().map(this::toDto).toList();
    }

    @Transactional
    public VendorDto createReference(String email, VendorDto input) {
        User user = getAuthenticatedUser(email);

        if (input.name == null || input.name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name/merchant is required");
        }

        validateUrl(input.paymentUrl);

        Vendor v = new Vendor();
        v.setUser(user);
        v.setName(input.name.trim());
        v.setCategory(input.category != null ? input.category.trim() : "Other");
        v.setIcon(input.icon != null ? input.icon : "storefront");
        v.setAccent(input.accent != null ? input.accent : "lavender");
        v.setUpiHandle(input.upiHandle != null ? input.upiHandle.trim() : null);
        v.setPaymentUrl(input.paymentUrl != null ? input.paymentUrl.trim() : null);
        v.setQrCodeRef(input.qrCodeRef != null ? input.qrCodeRef.trim() : null);
        v.setNotes(input.notes != null ? input.notes.trim() : null);
        v.setDefaultAmount(input.defaultAmount != null && input.defaultAmount >= 0 ? input.defaultAmount : 0.0);
        v.setIsQuickPay(Boolean.TRUE.equals(input.isQuickPay));

        Vendor saved = vendorRepository.save(v);
        return toDto(saved);
    }

    public VendorDto getReference(String email, Long id) {
        User user = getAuthenticatedUser(email);
        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment reference not found"));

        if (!v.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to payment reference");
        }

        return toDto(v);
    }

    @Transactional
    public VendorDto updateReference(String email, Long id, VendorDto input) {
        User user = getAuthenticatedUser(email);
        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment reference not found"));

        if (!v.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to payment reference");
        }

        if (input.name != null && !input.name.trim().isEmpty()) {
            v.setName(input.name.trim());
        }
        if (input.category != null) {
            v.setCategory(input.category.trim());
        }
        if (input.icon != null) {
            v.setIcon(input.icon);
        }
        if (input.accent != null) {
            v.setAccent(input.accent);
        }
        if (input.upiHandle != null) {
            v.setUpiHandle(input.upiHandle.trim());
        }
        if (input.paymentUrl != null) {
            validateUrl(input.paymentUrl);
            v.setPaymentUrl(input.paymentUrl.trim());
        }
        if (input.qrCodeRef != null) {
            v.setQrCodeRef(input.qrCodeRef.trim());
        }
        if (input.notes != null) {
            v.setNotes(input.notes.trim());
        }
        if (input.defaultAmount != null && input.defaultAmount >= 0) {
            v.setDefaultAmount(input.defaultAmount);
        }
        if (input.isQuickPay != null) {
            v.setIsQuickPay(input.isQuickPay);
        }

        Vendor updated = vendorRepository.save(v);
        return toDto(updated);
    }

    @Transactional
    public Map<String, Object> deleteReference(String email, Long id) {
        User user = getAuthenticatedUser(email);
        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment reference not found"));

        if (!v.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to payment reference");
        }

        vendorRepository.delete(v);

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("status", "success");
        res.put("message", "Payment reference deleted");
        res.put("deleted_id", id);
        return res;
    }

    private VendorDto toDto(Vendor v) {
        VendorDto dto = new VendorDto();
        dto.id = v.getId();
        dto.name = v.getName();
        dto.category = v.getCategory();
        dto.icon = v.getIcon();
        dto.accent = v.getAccent();
        dto.upiHandle = v.getUpiHandle();
        dto.paymentUrl = v.getPaymentUrl();
        dto.qrCodeRef = v.getQrCodeRef();
        dto.notes = v.getNotes();
        dto.defaultAmount = v.getDefaultAmount();
        dto.isQuickPay = v.getIsQuickPay();
        return dto;
    }
}
