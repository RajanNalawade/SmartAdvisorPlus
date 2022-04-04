package sbilife.com.pointofsale_bancaagency.home.e_mhr_format;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooter extends PdfPageEventHelper {

    Font normalfont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    Font boldfont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);

    String[] strData;

    public HeaderFooter() {
    }

    public HeaderFooter(String[] strData) {
        this.strData = strData;
    }

    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfContentByte cb = writer.getDirectContent();
            //Phrase header = new Phrase("this is a header", ffont);
            //Phrase footer1 = new Phrase(footerText, boldfont);

            boldfont.setColor(BaseColor.GRAY);
            normalfont.setColor(BaseColor.GRAY);

            /*ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("*Disclaimer: This is a computer generated certificate and does not require signature.", boldfont),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() +40 , 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("SBI Life Insurance Company Limited,", boldfont),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom()+ 30, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("Central Processing Centre : 7 level (D- Wing), 8th Level Seawoods Grand Central, Tower 2, Plot No. R-1 ", normalfont),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom()+20, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("Sector 40, Seawoods, Nerul Node, Dist Thane, Navi Mumbai - 400 706.", normalfont),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom()+10, 0);*/

            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase(strData[0], normalfont),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 0, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase(strData[1], normalfont),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase(strData[2], normalfont),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 20, 0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*public void onEndPage(PdfWriter writer,Document document) {
        Rectangle rect = writer.getBoxSize("art");
        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase(strData[0]), rect.getLeft(), rect.getBottom(), 0);
        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase(strData[1]), rect.getRight(), rect.getBottom(), 0);
    }*/

    private Phrase footer() {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
        Phrase p = new Phrase("this is a footer", ffont);
        return p;
    }
}
