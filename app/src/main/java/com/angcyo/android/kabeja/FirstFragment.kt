package com.angcyo.android.kabeja

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.angcyo.android.kabeja.databinding.FragmentFirstBinding
import com.angcyo.kabeja.library.Dxf
import com.angcyo.library.ex.toMsTime
import com.angcyo.svg.Svg
import com.pixplicity.sharp.SharpDrawable
import java.io.File
import java.io.FileOutputStream
import kotlin.system.measureTimeMillis

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layout.forEach {
            if (it.tag != null) {
                it.setOnClickListener(::onButtonClick)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onButtonClick(view: View) {
        val name = view.tag as String
        val outputFile = libCacheFile(requireContext(), "${name}.svg")
        context?.assets?.open(name)?.let {
            val time = measureTimeMillis {
                val outputStream = FileOutputStream(outputFile)
                Dxf.parse(it, outputStream)
                val svg = outputFile.readText()
                val drawable =
                    if (binding.pathBox.isChecked) svg.toSvgPathSharpDrawable() else svg.toSvgSharpDrawable()
                binding.imageView.setImageDrawable(drawable)
            }
            binding.textView.text = "->${outputFile}\n耗时:${time.toMsTime()}"
        }
    }
}

/**获取一个缓存文件*/
fun libCacheFile(context: Context, name: String, folderName: String = ""): File {
    return File(libCacheFolderPath(folderName, context), name)
}

/**缓存目录, 可以在应用详情中, 通过清理缓存清除.*/
fun libCacheFolderPath(folderName: String = "", context: Context): String {
    val folderFile = File(context.externalCacheDir ?: context.cacheDir, folderName)
    if (!folderFile.exists()) {
        folderFile.mkdirs()
    }
    return folderFile.absolutePath
}

fun String.toSvgSharpDrawable(): SharpDrawable? = if (isEmpty()) null else Svg.loadSvgDrawable(this)

fun String.toSvgPathSharpDrawable(): SharpDrawable? =
    if (isEmpty()) null else Svg.loadSvgPathDrawable(
        this,
        -1,
        null,
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = Color.BLACK
            this.style = Paint.Style.STROKE
            strokeWidth = 1f
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        },
        0,
        0
    )