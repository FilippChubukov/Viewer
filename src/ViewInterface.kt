import java.awt.image.BufferedImage

interface ViewInterface {

    fun View(img: BufferedImage)

}

interface Observer {

         fun change(img: BufferedImage)

}
