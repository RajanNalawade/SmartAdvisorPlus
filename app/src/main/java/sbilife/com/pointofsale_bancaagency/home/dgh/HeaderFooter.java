package sbilife.com.pointofsale_bancaagency.home.dgh;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooter extends PdfPageEventHelper {

    Font normalfont = new Font(FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    Font boldfont = new Font(FontFamily.TIMES_ROMAN, 8, Font.BOLD);

    String footerText;


    public void onEndPage(PdfWriter writer, Document document) {
        try {
           /* PdfContentByte cb = writer.getDirectContent();
            //Phrase header = new Phrase("this is a header", ffont);
            //Phrase footer1 = new Phrase(footerText, boldfont);

            boldfont.setColor(BaseColor.GRAY);
            normalfont.setColor(BaseColor.GRAY);

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                new Phrase("SBI Life Insurance Company Limited,", boldfont),
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom()+ 30, 0);

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("http://www.xxxx-your_example.com/"),
                    110, 30, 0);

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("Registered & Corporate Office: Natraj, M.V. Road, & Western Express Highway Junction, Andheri (East), Mumbai – 400 069. IRDAI Registration No 111, Toll Free: 1800 267 9090 ", normalfont),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom()+20, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("Email: info@sbilife.co.in   |  Website:  www.sbilife.co.in  |  CIN: L99999MH2000PLC129113", normalfont),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom()+20, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                new Phrase("PS-10.Ver.07 11-19 ENG (Draft)", normalfont),
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom()+10, 0);*/

            footer(writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void footer(PdfWriter writer) {
        PdfPTable footer = new PdfPTable(1);
        try {

            footer.setWidths(new int[]{100});
            footer.setTotalWidth(527);
            footer.setLockedWidth(false);
            footer.getDefaultCell().setFixedHeight(35);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.BLACK);
            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Font footerTextcolor = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
            footerTextcolor.setColor(BaseColor.LIGHT_GRAY);
            footer.addCell(new Phrase(String.format("SBI Life Insurance Company Limited,Registered & Corporate Office: Natraj, M.V. Road, & Western Express Highway Junction, Andheri (East), Mumbai – 400 069. IRDAI Registration No 111, Toll Free: 1800 267 9090 | Email: info@sbilife.co.in   |  Website:  www.sbilife.co.in  |  CIN: L99999MH2000PLC129113"),
                    footerTextcolor));

            footer.addCell(new Phrase(String.format("Page - " + writer.getPageNumber()), new Font(FontFamily.HELVETICA, 12, Font.BOLD)));

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

}
