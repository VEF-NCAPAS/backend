package me.workhive.workhive.services.impl;

import me.workhive.workhive.services.EmailTemplateService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    @Override
    public String appliedTemplate(String candidateName, String vacancyTitle, String companyName) {

        return """
        <html>
        <body style="font-family: Arial, sans-serif">

            <h2>Postulación Enviada</h2>

            <p>
                Estimado/a <b>%s</b>,
            </p>

            <p>
                Su postulación ha sido enviada exitosamente.
            </p>

            <p>
                <b>Vacante:</b> %s
            </p>

            <p>
                <b>Empresa:</b> %s
            </p>

            <p>
                Le notificaremos cualquier actualización relacionada con su postulación.
            </p>

            <hr>

            <p>
                Saludos cordiales,<br>
                <b>%s</b>
            </p>

            <p style="color: gray; font-size: 12px;">
                Esta notificación fue enviada a través de WorkHive.
            </p>

        </body>
        </html>
        """
                .formatted(candidateName, vacancyTitle, companyName, companyName);
    }

    @Override
    public String reviewedTemplate(String candidateName, String vacancyTitle) {

        return """
        <html>
        <body style="font-family: Arial, sans-serif">

            <h2>Postulación Revisada</h2>

            <p>
                Estimado/a <b>%s</b>,
            </p>

            <p>
                Le informamos que su postulación para:
            </p>

            <p>
                <b>%s</b>
            </p>

            <p>
                ha sido revisada por el equipo de reclutamiento.
            </p>

            <p>
                Su candidatura continúa en evaluación y será notificado/a sobre los próximos pasos del proceso.
            </p>

            <hr>

        </body>
        </html>
        """
                .formatted(candidateName, vacancyTitle);
    }

    @Override
    public String interviewTemplate(String candidateName, String vacancyTitle, LocalDateTime interviewDate, String meetingLink) {

        return """
        <html>
        <body style="font-family: Arial, sans-serif">

            <h2>Entrevista Programada</h2>

            <p>
                Estimado/a <b>%s</b>,
            </p>

            <p>
                Su postulación para la vacante
                <b>%s</b>
                ha avanzado a la etapa de entrevista.
            </p>

            <p>
                <b>Fecha y hora:</b> %s
            </p>

            <p>
                <b>Enlace de reunión:</b>
                <a href="%s">Unirse a la entrevista</a>
            </p>

            <hr>

            <p>
                Le deseamos mucho éxito en su entrevista.
            </p>

            <p style="color: gray; font-size: 12px;">
                Esta notificación fue enviada a través de WorkHive.
            </p>

        </body>
        </html>
        """
                .formatted(candidateName, vacancyTitle, interviewDate, meetingLink);
    }

    @Override
    public String technicalTestTemplate(String candidateName, String vacancyTitle,
                                        String testLink, LocalDateTime deadline) {

        return """
        <html>
        <body style="font-family: Arial, sans-serif">

            <h2>Prueba Técnica Asignada</h2>

            <p>
                Estimado/a <b>%s</b>,
            </p>

            <p>
                Se le ha asignado una prueba técnica para la vacante:
            </p>

            <p>
                <b>%s</b>
            </p>

            <p>
                <b>Fecha límite:</b> %s
            </p>

            <p>
                <a href="%s">Acceder a la prueba técnica</a>
            </p>

            <hr>

            <p style="color: gray; font-size: 12px;">
                Esta notificación fue enviada a través de WorkHive.
            </p>

        </body>
        </html>
        """
                .formatted(candidateName, vacancyTitle, deadline, testLink);
    }

    @Override
    public String selectedTemplate(String candidateName, String vacancyTitle) {

        return """
        <html>
        <body style="font-family: Arial, sans-serif">

            <h2>¡Felicidades!</h2>

            <p>
                Estimado/a <b>%s</b>,
            </p>

            <p>
                Nos complace informarle que ha sido
                <b>seleccionado/a</b>
                para la vacante:
            </p>

            <p>
                <b>%s</b>
            </p>

            <p>
                La empresa se pondrá en contacto con usted próximamente para brindarle más información sobre los siguientes pasos.
            </p>

            <hr>

            <p style="color: gray; font-size: 12px;">
                Esta notificación fue enviada a través de WorkHive.
            </p>

        </body>
        </html>
        """
                .formatted(candidateName, vacancyTitle);
    }

    @Override
    public String rejectedTemplate(String candidateName, String vacancyTitle) {

        return """
        <html>
        <body style="font-family: Arial, sans-serif">

            <h2>Actualización de Postulación</h2>

            <p>
                Estimado/a <b>%s</b>,
            </p>

            <p>
                Gracias por participar en el proceso de selección para:
            </p>

            <p>
                <b>%s</b>
            </p>

            <p>
                Después de una cuidadosa evaluación, hemos decidido continuar con otros candidatos.
            </p>

            <p>
                Agradecemos sinceramente su interés y le deseamos muchos éxitos en futuras oportunidades.
            </p>

            <hr>

            <p style="color: gray; font-size: 12px;">
                Esta notificación fue enviada a través de WorkHive.
            </p>

        </body>
        </html>
        """
                .formatted(candidateName, vacancyTitle);
    }

    @Override
    public String withdrawnTemplate(String candidateName, String vacancyTitle) {

        return """
        <html>
        <body style="font-family: Arial, sans-serif">

            <h2>Postulación Retirada</h2>

            <p>
                Estimado/a <b>%s</b>,
            </p>

            <p>
                Su postulación para:
            </p>

            <p>
                <b>%s</b>
            </p>

            <p>
                ha sido retirada exitosamente.
            </p>

            <hr>

            <p>
                Saludos cordiales,<br>
                WorkHive
            </p>

        </body>
        </html>
        """
                .formatted(candidateName, vacancyTitle);
    }
}