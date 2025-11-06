package ir.amirroid.canvas

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform