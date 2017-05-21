import java.awt.Color
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.awt.image.BufferedImage

class BMP24Model: ModelInterface, Observable {

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

            imageStructure.put("bfType", getBytes(fileBytes, 0x00, 2))
            imageStructure.put("bfSize", getBytes(fileBytes, 0x02, 4))
            imageStructure.put("bfOffBits", getBytes(fileBytes, 0x0A, 4))

            imageStructure.put("biSize", getBytes(fileBytes, 0x0E, 4))
            imageStructure.put("biWidth", getBytes(fileBytes, 0x12, 4))
            imageStructure.put("biHeight", getBytes(fileBytes, 0x16, 4))
            imageStructure.put("biPlanes", getBytes(fileBytes, 0x1A, 2))
            imageStructure.put("biBitCount", getBytes(fileBytes, 0x1C, 2))
            imageStructure.put("biCompression", getBytes(fileBytes, 0x1E, 2))
            imageStructure.put("biSizeImage", getBytes(fileBytes, 0x22, 4))
            imageStructure.put("biXPelsPerMeter", getBytes(fileBytes, 0x26, 4))
            imageStructure.put("biYPelsPerMeter", getBytes(fileBytes, 0x2A, 4))
            imageStructure.put("biClrUsed", getBytes(fileBytes, 0x2E, 4))
            imageStructure.put("biClrImportant", getBytes(fileBytes, 0x32, 4))
        }

        else {

            println("Sorry, this BMP's version is not supported")
            return

        }

        BytesToImage(fileBytes)
        notifyObservers()
    }

    override fun BytesToImage(fileBytes: ByteArray) { // Здесь 3 байта определяют 3 компоненты цвета, просто читаем цвета.
        val pixels = fileBytes.copyOfRange(imageStructure["bfOffBits"]!!.toInt(), imageStructure["bfSize"]!!.toInt())

        val width = imageStructure["biWidth"]!!.toInt()
        val height = imageStructure["biHeight"]!!.toInt()


        img = BufferedImage(width, height, 1)

        var index = pixels.size - 1
        val fix = (index + 1) / height - 3 * width

        for (i in 0..height - 1) {

            index -= fix


            for (j in width - 1 downTo 0) {

                var R = pixels[index--].toInt()

                if (R < 0) R += 256

                var G = pixels[index--].toInt()

                if (G < 0) G += 256

                var B = pixels[index--].toInt()
                if (B < 0) B += 256

                val Colors = Color(R, G, B)

                img!!.setRGB(j, i, Colors.rgb)
            }
        }
    }

    private fun getBytes(byteArray: ByteArray, start: Int, length: Int): Long {

        var data: Long = 0

        for (i in length - 1 downTo 0) {

            var tmp: Long = byteArray[start + i].toLong()

            if (tmp < 0) tmp += 256

            data = data shl 8
            data += tmp

        }

        return data
    }
}



