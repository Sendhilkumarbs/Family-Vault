package com.bankmanager.service;

import com.bankmanager.model.BankAccount;
import com.bankmanager.model.CreditCard;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public void exportToExcel(List<BankAccount> accounts, OutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Bank Accounts");

            // ── Styles ───────────────────────────────────────────
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle altDataStyle = createAltDataStyle(workbook);

            // ── Title Row ────────────────────────────────────────
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Family Vault — Bank Accounts — Generated on " +
                    java.time.LocalDateTime.now().format(DATE_FMT));
            titleCell.setCellStyle(titleStyle);
            titleRow.setHeightInPoints(30);

            // ── Determine max extra credit cards ──────────────────
            int maxExtraCards = 0;
            for (BankAccount acct : accounts) {
                if (acct.getExtraCreditCards() != null && acct.getExtraCreditCards().size() > maxExtraCards) {
                    maxExtraCards = acct.getExtraCreditCards().size();
                }
            }

            // ── Column Headers ───────────────────────────────────
            java.util.List<String> headerList = new java.util.ArrayList<>(java.util.Arrays.asList(
                "S.No",
                "Person Name", "Relationship",
                "Bank Name", "Branch", "Customer ID", "Account Number", "Account Type", "IFSC Code", "MICR Code",
                "Joint Holder Name", "Joint Holder Relationship",
                "Net Banking User ID", "Net Banking Password", "Transaction Password",
                "Registered Mobile", "Mobile Banking PIN", "UPI ID",
                "Debit Card Number", "Debit Card PIN", "Debit Card Expiry", "Debit Card CVV",
                "Credit Card Number", "Credit Card PIN", "Credit Card Expiry", "Credit Card CVV", "Credit Card Limit"
            ));

            // Add headers for extra credit cards
            for (int i = 1; i <= maxExtraCards; i++) {
                headerList.add("Extra CC " + i + " Name");
                headerList.add("Extra CC " + i + " Number");
                headerList.add("Extra CC " + i + " PIN");
                headerList.add("Extra CC " + i + " Expiry");
                headerList.add("Extra CC " + i + " CVV");
                headerList.add("Extra CC " + i + " Limit");
            }

            headerList.add("Nominee Name");
            headerList.add("Nominee Relationship");
            headerList.add("Email Linked");
            headerList.add("Remarks");

            String[] headers = headerList.toArray(new String[0]);

            // Merge title row across all columns
            if (headers.length > 1) {
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));
            }

            Row headerRow = sheet.createRow(1);
            headerRow.setHeightInPoints(22);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ── Data Rows ────────────────────────────────────────
            int rowNum = 2;
            int serial = 1;
            for (BankAccount acct : accounts) {
                Row row = sheet.createRow(rowNum);
                CellStyle style = (rowNum % 2 == 0) ? dataStyle : altDataStyle;
                int col = 0;

                createCell(row, col++, String.valueOf(serial++), style);
                createCell(row, col++, acct.getPersonName(), style);
                createCell(row, col++, acct.getRelationship(), style);
                createCell(row, col++, acct.getBankName(), style);
                createCell(row, col++, acct.getBranchName(), style);
                createCell(row, col++, acct.getCustomerId(), style);
                createCell(row, col++, acct.getAccountNumber(), style);
                createCell(row, col++, acct.getAccountType(), style);
                createCell(row, col++, acct.getIfscCode(), style);
                createCell(row, col++, acct.getMicrCode(), style);
                createCell(row, col++, acct.getJointHolderName(), style);
                createCell(row, col++, acct.getJointHolderRelationship(), style);
                createCell(row, col++, acct.getNetBankingUserId(), style);
                createCell(row, col++, acct.getNetBankingPassword(), style);
                createCell(row, col++, acct.getTransactionPassword(), style);
                createCell(row, col++, acct.getRegisteredMobileNumber(), style);
                createCell(row, col++, acct.getMobileBankingPin(), style);
                createCell(row, col++, acct.getUpiId(), style);
                createCell(row, col++, acct.getDebitCardNumber(), style);
                createCell(row, col++, acct.getDebitCardPin(), style);
                createCell(row, col++, acct.getDebitCardExpiryDate(), style);
                createCell(row, col++, acct.getDebitCardCvv(), style);
                createCell(row, col++, acct.getCreditCardNumber(), style);
                createCell(row, col++, acct.getCreditCardPin(), style);
                createCell(row, col++, acct.getCreditCardExpiryDate(), style);
                createCell(row, col++, acct.getCreditCardCvv(), style);
                createCell(row, col++, acct.getCreditCardLimit(), style);

                // Extra credit cards
                java.util.List<CreditCard> extraCards = acct.getExtraCreditCards();
                for (int i = 0; i < maxExtraCards; i++) {
                    if (extraCards != null && i < extraCards.size()) {
                        CreditCard card = extraCards.get(i);
                        createCell(row, col++, card.getCardName(), style);
                        createCell(row, col++, card.getCardNumber(), style);
                        createCell(row, col++, card.getCardPin(), style);
                        createCell(row, col++, card.getExpiryDate(), style);
                        createCell(row, col++, card.getCvv(), style);
                        createCell(row, col++, card.getCreditLimit(), style);
                    } else {
                        // Empty cells for accounts with fewer extra cards
                        for (int j = 0; j < 6; j++) {
                            createCell(row, col++, "", style);
                        }
                    }
                }

                createCell(row, col++, acct.getNomineeName(), style);
                createCell(row, col++, acct.getNomineeRelationship(), style);
                createCell(row, col++, acct.getEmailLinked(), style);
                createCell(row, col++, acct.getRemarks(), style);

                rowNum++;
            }

            // ── Auto-size columns ────────────────────────────────
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // Ensure minimum width
                if (sheet.getColumnWidth(i) < 3500) {
                    sheet.setColumnWidth(i, 3500);
                }
            }

            // ── Freeze header rows ───────────────────────────────
            sheet.createFreezePane(0, 2);

            workbook.write(outputStream);
        }
    }

    // ── Helper: create a cell ────────────────────────────────────
    private void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    // ── Styles ───────────────────────────────────────────────────

    private CellStyle createTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(style);
        return style;
    }

    private CellStyle createDataStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        setBorders(style);
        return style;
    }

    private CellStyle createAltDataStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setBorders(style);
        return style;
    }

    private void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}
