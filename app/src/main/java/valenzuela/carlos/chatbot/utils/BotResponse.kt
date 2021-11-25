package valenzuela.carlos.chatbot.utils

import valenzuela.carlos.chatbot.utils.Constans.OPEN_GOOGLE
import valenzuela.carlos.chatbot.utils.Constans.OPEN_SEARCH
import java.lang.Exception
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {
    fun basicResponse(_message: String): String {
        val random = (0..2).random()
        val message = _message.toLowerCase()

        return when {
            //Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"

                "I flipped a coin and it landed on $result"
            }

            //Math calculations
            message.contains("solve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMaths.solveMath(equation ?: "0")
                    "$answer"
                } catch (e: Exception) {
                    "Sorry, I can't solve that"
                }
            }

            //HELLO
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hi"
                    1 -> "Hello"
                    2 -> "Sea wave"
                    else -> "error!"
                }
            }

            //How are you?
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine"
                    1 -> "I'm hungry..."
                    2 -> "Pretty good!"
                    else -> "error!"
                }
            }

            //Time
            message.contains("time") && message.contains("?") -> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(java.util.Date(timeStamp.time))

                date.toString()
            }

            //Open Google
            message.contains("open") && message.contains("google") -> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("search") -> {
                OPEN_SEARCH
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "I don't understand..."
                    1 -> "Try asking me something different"
                    2 -> "Idk man"
                    else -> "error"
                }
            }
        }
    }
}
