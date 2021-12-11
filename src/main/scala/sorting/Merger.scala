package sorting

import sorting.Sorter.{sort, splitSingleInput}
import common.FileHandler

import java.io.File
import java.nio.file.{Files, Paths, StandardCopyOption}
import scala.util.Try

object Merger {

  // split and merge ~/shuffle-workerId-## files with subRanges (unsorted)
  // merge shuffled files into output-##
  def merge(workerPath: String, outputPath: String, subRanges: Seq[(String, String)]): Any = {
    val workerDir = new File(workerPath)
    val listOfInputFiles = FileHandler.getListOfStageFiles(workerPath, "shuffle-")
    for (file <- listOfInputFiles) {
      splitSingleInput(file.getPath, outputPath + "/output.", "", Iterator.from(0).zip(subRanges).toMap)
    }
  }

  def sortNotTagged(unsortedFilePath: String): Any = {
    tagAsUnsorted(unsortedFilePath)
    sort(unsortedFilePath + "-unsorted")
  }

  def tagAsUnsorted(oldName: String): Boolean = Try(new File(oldName).renameTo(new File(oldName + "-unsorted"))).getOrElse(false)

  // move partition-workerId-## files to each worker's shuffle directory with name of ~/shuffle-workerId-##
  // on network
  // for test
  def shuffle(workerPath: String): Any = {
    val listOfPartitionedFiles = FileHandler.getListOfStageFiles(workerPath, "partition")
    for (file <- listOfPartitionedFiles) {
      // under 10 workers exists
      val workerId = file.getPath.split("-")(1).toInt
      val path = Files.move(
        Paths.get(file.getPath),
        Paths.get(workerPath + workerId + "/shuffle-" + workerId + "-1"),
        StandardCopyOption.REPLACE_EXISTING)
    }
  }
}
