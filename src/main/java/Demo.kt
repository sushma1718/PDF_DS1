import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.PDResources
import java.awt.Color.black
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.PDPageContentStream
import java.awt.Color
import java.awt.FontFormatException
import java.io.IOException
import java.io.File


object Demo {

    private const val NAME = "Govardhan"

    private const val TEMPLATE = "_DATE_ \n" +
            "\n" +
            "\n" +
            "Dear _APPLICANT_NAME_,\n" +
            "\n" +
            "We are pleased to offer you employment at _COMPANY_NAME_. We feel that your skills and background will be valuable assets to our team. \n" +
            "\n" +
            "Sincerely,\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "_ADMIN_NAME_\n" +
            "_COMPANY_NAME_"



    @Throws(IOException::class, FontFormatException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Util.convertTemplateToData(TEMPLATE)
        newPdf(data, "Demo.pdf")
        addSignatureField("Demo.pdf")
        //newPdf(TEMPLATE, "Demo.pdf")
        //getMetaData("jo.pdf")
    }

    @Throws(IOException::class, FontFormatException::class)
    @JvmStatic
    private fun newPdf(pdfText: String, fileName: String) {
        val doc = PDDocument()
        val page = PDPage(PDRectangle.A4)
        doc.addPage(page)
        val font = PDType1Font.COURIER
        val contentStream = PDPageContentStream(doc, page)
        contentStream.beginText()
        contentStream.setFont(font, 14f)
        contentStream.setLeading(14.5f);
        contentStream.setNonStrokingColor(Color.black)
        contentStream.newLineAtOffset(25f, 700f)
        Util.getLines(pdfText).forEach {
            contentStream.showText(it)
            contentStream.newLine()
        }
        contentStream.endText()
        contentStream.close()
        doc.save(fileName)
        doc.close()
    }

    private fun addSignatureField(filePath: String) {

        val document = PDDocument.load(File(filePath))
        val page = document.getPage(0)

        // Adobe Acrobat uses Helvetica as a default font and
        // stores that under the name '/Helv' in the resources dictionary
        val font = PDType1Font.HELVETICA
        val resources = PDResources()
        resources.put(COSName.getPDFName("Helv"), font)

        // Add a new AcroForm and add that to the document
        val acroForm = PDAcroForm(document)
        document.documentCatalog.acroForm = acroForm

        // Add and set the resources and default appearance at the form level
        acroForm.defaultResources = resources

        // Acrobat sets the font size on the form level to be
        // auto sized as default. This is done by setting the font size to '0'
        val defaultAppearanceString = "/Helv 0 Tf 0 g"
        acroForm.defaultAppearance = defaultAppearanceString
        // --- end of general AcroForm stuff ---

        // Create empty signature field, it will get the name "Signature1"
        val signatureField = PDSignatureField(acroForm)
        val widget = signatureField.widgets[0]


        //The height where the signature field needs to be placed can also be calculated according to the number of lines in the content.
        val rect = PDRectangle(25f, 500f, 150f, 50f)
        widget.rectangle = rect
        widget.page = page
        page.annotations.add(widget)

        acroForm.fields.add(signatureField)

        document.save("Demo.pdf")
        document.close()

    }

    @JvmStatic
    private fun getMetaData(filePath: String) {
        val doc = PDDocument.load(File(filePath))
        val acroForm = doc.documentCatalog.acroForm
        System.out.println(acroForm)
    }


}