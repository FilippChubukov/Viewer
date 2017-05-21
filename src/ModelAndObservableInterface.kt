interface ModelInterface {

    fun parse(filepath: String)

    fun BytesToImage(fileBytes: ByteArray)

}


interface Observable {

    fun addObserver(obs: Observer)

    fun removeObserver(obs: Observer)

    fun notifyObservers()

}