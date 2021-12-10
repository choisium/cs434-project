package sorting

import java.io.{File, PrintWriter}
import scala.io.Source
import common.FileHandler

object Sampler {
  def sample(inputPath: String, workerPath: String, sampleSize: Int): Unit = {
    try {
      val bufferedSource = Source.fromFile(inputPath + "/input-1")
      val sampleWriter = new PrintWriter(new File(workerPath + "/sample"))

      for (line <- bufferedSource.getLines.take(sampleSize)) {
        sampleWriter.write(line + "\n")
      }

      bufferedSource.close
      sampleWriter.close

    } catch {
      case ex: Exception => println(ex)
    }
  }

}
