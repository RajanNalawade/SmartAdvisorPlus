package sbilife.com.pointofsale_bancaagency.home.mhr;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooter extends PdfPageEventHelper {

    Font normalfont = new Font(FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    Font boldfont = new Font(FontFamily.TIMES_ROMAN, 8, Font.BOLD);

    String footerText;


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
                document.bottom()+10, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                new Phrase("Board No. : 022 6645 6000 Fax : 022 6645 6653 Toll Free Number 18002679090 (9 am - 9 pm, 7 days a week).", normalfont),
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom()- 0, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                new Phrase("Website : www.sbilife.co.in. IRDAI Regn. No 111. CIN: L99999MH2000PLC129113", normalfont),
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom()- 10, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                new Phrase("Regd. Office : “Natraj”, M.V. Road & Western Express Highway Junction, Andheri (East), Mumbai – 400069.", normalfont),
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom()-20, 0);*/


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Phrase footer() {
        Font ffont = new Font(FontFamily.UNDEFINED, 5, Font.ITALIC);
        Phrase p = new Phrase("this is a footer", ffont);
        return p;
    }
}
