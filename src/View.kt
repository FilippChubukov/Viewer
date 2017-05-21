
import java.awt.image.BufferedImage
import javax.swing.JFrame
import java.awt.Graphics
import java.awt.Panel



class BmpViewer: ViewInterface, Observer {

    override fun change(img: BufferedImage) {

        View(img)

    }

    override fun View(img: BufferedImage) {

        val picture = JFrame()

        picture.setSize(img.width, img.height)
        picture.isVisible = true

        val panel = Draw(img)
        picture.contentPane.add(panel)



    }
}

class Draw(var bufferedImage: BufferedImage): Panel() {

         override fun paint(g: Graphics) {

        g.drawImage(bufferedImage, 0, 0, this)// Рисует изображение в левом верхнем углу.

    }
}

