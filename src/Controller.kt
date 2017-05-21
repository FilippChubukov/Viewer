
import sun.awt.image.ImageFormatException
import java.io.File
import java.io.FileInputStream

interface ControllerInterface {

    fun CheckFormat(filepath: String)

    fun chooseBMPmodel(filepath: String)

}

class BmpController (val ImageViewer: BmpViewer): ControllerInterface {

    override fun CheckFormat(filepath: String) {

        val Format = filepath.substring(filepath.length - 4)

        if (Format != ".bmp" && Format != ".dib") throw ImageFormatException("Wrong file type!")

    }


    override fun chooseBMPmodel(filepath: String) {

        val file = File(filepath)
        val Bytes = ByteArray(file.length().toInt())

        val FileInput = FileInputStream(file)
        FileInput.read(Bytes)
        FileInput.close()

        when {

            BMPtype(Bytes) == 8 -> {

                val model = BMP8Model()
                model.addObserver(ImageViewer)
                model.parse(filepath)

            }

            BMPtype(Bytes) == 24 -> {

                val model = BMP24Model()
                model.addObserver(ImageViewer)
                model.parse(filepath)

            }

            else -> {

                println("Sorry, this type of BMP doesn't supporting")

                return

            }
        }
    }

    private fun BMPtype(byteArray: ByteArray): Int {

        var data: Int = 0

        for (i in 1 downTo 0) {

            var tmp: Int = byteArray[0x1C + i].toInt()

            if (tmp < 0) tmp += 256

            data = data shl 8
            data += tmp
        }

        return data
    }
}
