package fr.deroffal.extract_georisques_icpe.batch.administration

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.JobOperator
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BatchController(
    val jobLauncher: JobLauncher,
    val import: Job
) {

    @GetMapping("/batch/launch")
    @ResponseStatus(OK)
    fun launch(@RequestParam("name") todo: String) {
        jobLauncher.run(
            import,
            JobParameters(
                mapOf(
                    "date" to JobParameter(Date())
                )
            )
        )
    }
}