package com.angcyo.kabeja.library

import org.kabeja.DraftDocument
import org.kabeja.dxf.parser.DXFParser
import org.kabeja.dxf.parser.DXFParserBuilder
import org.kabeja.svg.SVGGenerator
import org.kabeja.xml.SAXGenerator
import org.kabeja.xml.SAXPrettyOutputter
import org.xml.sax.ContentHandler
import java.io.*


/**
 * 2022-10-28
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @since 2022/10/28
 */
object Dxf {

    /**[inputStream] dxf文件输入流
     * [outputStream] svg数据输出流
     * */
    fun parse(inputStream: InputStream, outputStream: OutputStream, map: Map<Any?, Any?>? = null) {
        try {
            val parser: DXFParser = DXFParserBuilder.createDefaultParser() as DXFParser
            val doc: DraftDocument = parser.parse(inputStream, map ?: hashMapOf<Any, Any>())

            //the SVG will be emitted as SAX-Events
            //see org.xml.sax.ContentHandler for more information
            val handler: ContentHandler = SAXPrettyOutputter(outputStream)

            //the output - create first a SAXGenerator (SVG here)
            val generator: SAXGenerator = SVGGenerator()

            //setup properties
            generator.properties = HashMap()

            //start the output
            generator.generate(doc, handler, hashMapOf<String, Any>())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun parse(inputPath: String, outputPath: String, map: Map<Any?, Any?>? = null) {
        parse(FileInputStream(inputPath), FileOutputStream(outputPath), map)
    }

    /**转成svg字符串数据*/
    fun toSvg(inputPath: String, map: Map<Any?, Any?>? = null): String {
        val outputStream = ByteArrayOutputStream()
        parse(FileInputStream(inputPath), outputStream, map)
        return outputStream.toString("UTF-8")
    }
}