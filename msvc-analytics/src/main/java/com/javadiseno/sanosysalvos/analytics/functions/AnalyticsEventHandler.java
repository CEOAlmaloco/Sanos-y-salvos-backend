package com.javadiseno.sanosysalvos.analytics.functions;

import com.javadiseno.sanosysalvos.analytics.dtos.BusEventDTO;
import com.javadiseno.sanosysalvos.analytics.services.AnalyticsService;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsEventHandler implements Consumer<SQSEvent> {

	private final ObjectMapper objectMapper;
	private final AnalyticsService analyticsService;

	@Override
	public void accept(SQSEvent sqsEvent) {
		if (sqsEvent == null || sqsEvent.getRecords() == null) {
			log.warn("SQSEvent nulo o sin registros");
			return;
		}

		log.info("Recibidos {} registros SQS", sqsEvent.getRecords().size());

		for (SQSEvent.SQSMessage record : sqsEvent.getRecords()) {
			processRecord(record);
		}
	}

	private void processRecord(SQSEvent.SQSMessage record) {
		try {
			String body = record.getBody();
			log.debug("Procesando mensaje SQS: messageId={}", record.getMessageId());

			BusEventDTO busEventDTO = objectMapper.readValue(body, BusEventDTO.class);
			analyticsService.processEvent(busEventDTO);

		} catch (Exception e) {
			log.error("Error al procesar registro SQS messageId={}: {}", record.getMessageId(), e.getMessage());
		}
	}
}
