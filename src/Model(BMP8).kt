import java.awt.Color
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.awt.image.BufferedImage


class BMP8Model: ModelInterface, Observable {

    var img: BufferedImage? = null


    var observers = LinkedList<Observer>()

        override fun addObserver(obs: Observer) {

            observers.add(obs)
    }

        override fun removeObserver(obs: Observer) {

            observers.remove(obs)
    }

        override fun notifyObservers() {

            for (observer in observers) observer.change(img!!)
    }


    val imageStructure = HashMap<String, Long>()

    override fun parse(filepath: String) {

        val file = File(filepath)
        val fileBytes = ByteArray(file.length().toInt())

        val FileInput = FileInputStream(file)
        FileInput.read(fileBytes)
        FileInput.close()

        if (fileBytes[0].toChar() != 'B' || fileBytes[1].toChar() != 'M') {

            println("Sorry, this format is not supported")

            return
        }

        if (getBytes(fileBytes, 0x0E, 4).toInt() == 40) {
            //BITMAPFILEHEADER :
            imageStructure.put("bfType", getBytes(fileBytes, 0x00, 2))
            imageStructure.put("bfSize", getBytes(fileBytes, 0x02, 4))// Размер файла.
            // 4 зарезервированных байта с нулем.
            imageStructure.put("bfOffBits", getBytes(fileBytes, 0x0A, 4))// Положение пиксельных данных относительно начала данной структуры.
            //BITMAPINFO :
            imageStructure.put("biSize", getBytes(fileBytes, 0x0E, 4))// Размер данной структуры в байтах.
            imageStructure.put("biWidth", getBytes(fileBytes, 0x12, 4))// Ширина растра в пикселях.
            imageStructure.put("biHeight", getBytes(fileBytes, 0x16, 4))// Высота растра в пикселях.
            imageStructure.put("biPlanes", getBytes(fileBytes, 0x1A, 2))// Это поле используется в значках и курсорах Windows.
            imageStructure.put("biBitCount", getBytes(fileBytes, 0x1C, 2))// Количество бит на пиксель.
            imageStructure.put("biCompression", getBytes(fileBytes, 0x1E, 2))// Указывает на способ хранения пикселей.
            imageStructure.put("biSizeImage", getBytes(fileBytes, 0x22, 4))// Размер пиксельных данных в байтах.
            imageStructure.put("biXPelsPerMeter", getBytes(fileBytes, 0x26, 4))// Количество пикселей на метр по горизонтали.
            imageStructure.put("biYPelsPerMeter", getBytes(fileBytes, 0x2A, 4))// Количество пикселей на метр по вертикали.
            imageStructure.put("biClrUsed", getBytes(fileBytes, 0x2E, 4))// Размер таблицы цветов в ячейках.
            imageStructure.put("biClrImportant", getBytes(fileBytes, 0x32, 4))// Количество ячеек от начала таблицы цветов до последней используемой (включая её саму).
        }

        else {

            println("Sorry, this BMP's version unavailable")
            return
        }

        BytesToImage(fileBytes)
        notifyObservers()
    }

    override fun BytesToImage(fileBytes: ByteArray) {

        val width = imageStructure["biWidth"]!!.toInt()
        val height = imageStructure["biHeight"]!!.toInt()

        val TableOfColors = fileBytes.copyOfRange(0x36, imageStructure["bfOffBits"]!!.toInt() - 1)

        val pixels = fileBytes.copyOfRange(imageStructure["bfOffBits"]!!.toInt(), imageStructure["bfSize"]!!.toInt())


        img = BufferedImage(width, height, 1)

        var index = pixels.size - 1

        val fix = ((index + 1) / height) - width // Исправляет сдвиг.

        for (i in 0..height - 1) {

            index -= fix


            for (j in width - 1 downTo 0) {

                var pix = pixels[index--].toInt()

                if (pix < 0) pix += 256

                pix = pix shl 2

                var B = TableOfColors[pix++].toInt()

                if (B < 0) B += 256

                var G = TableOfColors[pix++].toInt()

                if (G < 0) G += 256

                var R = TableOfColors[pix].toInt()

                if (R < 0) R += 256

                val Colors = Color(R, G, B)

                img!!.setRGB(j, i, Colors.rgb)

            }
        }
    }

    private fun getBytes(bytes: ByteArray, start: Int, length: Int): Long {

        var data: Long = 0

        for (i in length - 1 downTo 0) {

            var tmp: Long = bytes[start + i].toLong()

            if (tmp < 0) tmp += 256

            data = data shl 8
            data += tmp
        }

        return data
    }

}



