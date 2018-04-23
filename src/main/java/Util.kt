import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sin

object Util {

    private const val LINE_LENGTH = 65

    @JvmStatic
    fun convertTemplateToData(template: String, applicantName: String = "John B Smith", companyName: String = "Swissth Solutions Inc.", adminName: String = "Bruce BatCave Admin"): String {
        return template
                .replace("_APPLICANT_NAME_", applicantName)
                .replace("_COMPANY_NAME_", companyName)
                .replace("_ADMIN_NAME_", adminName)
                .replace("_DATE_", SimpleDateFormat("d MMM yyyy").format(Date()))
    }

    @JvmStatic
    fun getLines(template: String): ArrayList<String> {
        val lines = ArrayList<String>()
        template.split("\n").forEach {
            if (it.length < LINE_LENGTH)
                lines.add(it)
            else
                lines.addAll(breakParaToLines(it))
        }
        return lines
    }

    private fun breakParaToLines(line: String): ArrayList<String> {
        val lines = ArrayList<String>()
        val words = line.split(" ")
        var singleLine = ""
        var wordCounter = 0;
        while (wordCounter < words.size) {
            if ("$singleLine ${words[wordCounter]}".length > LINE_LENGTH) {
                lines.add(singleLine.trim())
                singleLine = ""
            }
            singleLine = "$singleLine ${words[wordCounter]}"
            wordCounter++
        }
        lines.add(singleLine.trim())
        return lines
    }

}