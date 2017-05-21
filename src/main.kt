 fun main(args: Array<String>) {
        val bmpViewer = BmpViewer()

        while(true) {
            println("Path to file:")
            val path = readLine()!!

            BmpController(bmpViewer).CheckFormat(path)
            BmpController(bmpViewer).chooseBMPmodel(path)
        }
    }

