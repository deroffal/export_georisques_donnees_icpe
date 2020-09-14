package fr.deroffal.extract_georisques_icpe

import liquibase.integration.spring.SpringLiquibase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class LiquibaseConfiguration {

    @Bean
    fun liquibase(dataSource: DataSource): SpringLiquibase {
        val springLiquibase = SpringLiquibase()
        springLiquibase.dataSource = dataSource
        springLiquibase.changeLog = "classpath:changelog.xml"
        return springLiquibase
    }
}