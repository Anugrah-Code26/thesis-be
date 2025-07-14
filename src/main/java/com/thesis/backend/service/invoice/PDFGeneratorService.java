package com.thesis.backend.service.invoice;

import com.thesis.backend.entity.invoice.Invoice;
import com.itextpdf.text.DocumentException;

public interface PDFGeneratorService {
    public byte[] generateInvoicePdf(Invoice invoice) throws DocumentException;
}
