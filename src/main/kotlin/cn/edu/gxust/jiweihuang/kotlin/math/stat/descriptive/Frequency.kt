package cn.edu.gxust.jiweihuang.kotlin.math.stat.descriptive

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*


class Frequency<T : Comparable<T>> : org.hipparchus.stat.Frequency<T> {

    constructor() : super()

    constructor(comparator: Comparator<in T>) : super(comparator)

    fun add(value: T) {
        addValue(value)
    }

    fun addAll(data: Collection<T>) {
        data.forEach(this::addValue)
    }

    val counts: NavigableMap<T, Long>
        get() {
            val makeMap = TreeMap<T, Long>()
            val iterator = this.valuesIterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                makeMap[key] = getCount(key)
            }
            return makeMap
        }

    val frequency: NavigableMap<T, Double>
        get() {
            val makeMap = TreeMap<T, Double>()
            val iterator = this.valuesIterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                makeMap[key] = getPct(key)
            }
            return makeMap
        }

    override fun toString(): String {
        return "Counts:$counts\nFrequency:$frequency"
    }

    fun toCSV(csvFile: String) {
        val directory = File(File(csvFile).parent)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        lateinit var fileWriter: FileWriter
        try {
            fileWriter = FileWriter(csvFile)
            val iterator = this.valuesIterator()
            if (!iterator.hasNext()) return
            while (true) {
                val key = iterator.next()
                val count = getCount(key)
                val pct = getPct(key)
                with(fileWriter) {
                    write(key.toString())
                    write(",")
                    write(count.toString())
                    write(",")
                    write(pct.toString())
                }
                if (!iterator.hasNext()) {
                    break
                }
                fileWriter.write("\n")
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } finally {
            try {
                fileWriter.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}