import java.io.File
import java.io.FileInputStream
import java.util.ArrayList
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage
import javax.imageio.ImageIO.read



internal class BMP8ModelTest {

    fun Comparison (FirstImage: BufferedImage, SecondImage: BufferedImage): Boolean {

        if (FirstImage.width != SecondImage.width) return false

        if (SecondImage.height != SecondImage.height) return false


            for (i in 0..FirstImage.width - 1)

                for (j in 0..SecondImage.height - 1) {

                    if (FirstImage.getRGB(i, j) != SecondImage.getRGB(i, j)) return false

                }

        return true

    }

    @Test
    fun test() {

        val files = ArrayList<String>()

        files.add("C:/Users/Philippok/Desktop/8bit/freebsd2_8bit.bmp")
        files.add("C:/Users/Philippok/Desktop/8bit/bogts_8bit.bmp")

        for (path in files) {

            val file = File(path)
            val Bytes = ByteArray(file.length().toInt())

            val FileInput = FileInputStream(file)
            FileInput.read(Bytes)
            FileInput.close()

            val model = BMP8Model()
            model.parse(path)


            assertEquals(Comparison(model.img!!, read(file)), true)
        }
    }
}




