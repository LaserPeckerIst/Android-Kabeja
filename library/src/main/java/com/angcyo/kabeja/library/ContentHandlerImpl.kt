package com.angcyo.kabeja.library

import org.kabeja.xml.SAXPrettyOutputter
import java.io.FileOutputStream

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @since 2022/10/28
 */
class ContentHandlerImpl(filePath: String) : SAXPrettyOutputter() {
    init {
        setOutput(FileOutputStream(filePath))
    }
}