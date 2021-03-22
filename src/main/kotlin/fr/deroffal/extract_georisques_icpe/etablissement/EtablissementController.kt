package fr.deroffal.extract_georisques_icpe.etablissement

import fr.deroffal.extract_georisques_icpe.service.DateService
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class EtablissementController(
    private val etablissementModelMapper: EtablissementModelMapper,
    private val etablissementRepository: EtablissementRepository,
    private val jobLauncher: JobLauncher,
    private val dateService: DateService,
    private val importJob: Job
) {

    @GetMapping("/etablissement/list")
    @ResponseStatus(OK)
    fun etablissements(): MutableIterable<EtablissementModel> =
        etablissementModelMapper.toModel(etablissementRepository.findAll())

    @GetMapping("/etablissement/sync")
    @ResponseStatus(OK)
    fun sync(
        @RequestParam("parametreGeographiqueExport") parametreGeographiqueExport: String?,
        @RequestParam("exclureSeveso") exclureSeveso: Boolean?
    ) {

        val parameters: MutableMap<String, JobParameter> =
            mutableMapOf("dateSynchronisation" to JobParameter(Date.from(dateService.now())))
        if (parametreGeographiqueExport != null) {
            parameters["parametreGeographiqueExport"] = JobParameter(parametreGeographiqueExport)
        }
        if (exclureSeveso != null) {
            parameters["exclureSeveso"] = JobParameter(exclureSeveso.toString())
        }

        jobLauncher.run(
            importJob,
            JobParameters(parameters)
        )
    }
}
