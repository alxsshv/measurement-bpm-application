package com.github.alxsshv.measurementbpmapplication.arshinclient;

import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.Response;
import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.mit.MitItem;
import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.mit.MitResponse;
import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.vri.VriResponse;
import com.github.alxsshv.measurementbpmapplication.arshinclient.dto.vri.VriResult;
import com.github.alxsshv.measurementbpmapplication.arshinclient.exception.ArshinClientResponseException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import static com.github.alxsshv.measurementbpmapplication.reports.utils.WaitManager.waitMillis;


@Getter
@Setter
@Slf4j
public class ArshinHttpClient {

    private static final int FAIL_COUNT_LIMIT = 5;

    private ArshinHttpClient() {
    }

    //TODO: переписать на ретраи
    public static VriResult getVerificationItems(String verificationRequest, int failCount) throws ArshinClientResponseException, HttpServerErrorException {
        log.info("Запрос в ФГСИ Аршин: {}", verificationRequest);
        try {
            RestClient restClient = RestClient.create();
            VriResponse response = restClient
                    .get()
                    .uri(verificationRequest)
                    .retrieve()
                    .body(VriResponse.class);
            if (response != null && response.getResult().getCount() > 0) {
                return response.getResult();
            }
            throw new ArshinClientResponseException(getErrorMessage(response));
        } catch (ResourceAccessException ex) {
            log.error(ex.getMessage());
            throw new ArshinClientResponseException("Ошибка соединения с сервером ФГИС \"Аршин\". Проверьте интернет соединение и доступность серверов ФГИС \"Аршин\"");
        } catch (HttpClientErrorException ex) {
            log.error(ex.getMessage());
            throw new ArshinClientResponseException("Ошибка взаимодейтсвия ПО с сервером ФГИС \"Аршин\". Пожалуйста повторите попытку");
        } catch (HttpServerErrorException ex) {
            if (failCount < FAIL_COUNT_LIMIT) {
                failCount++;
                int timeMillis = (int) (5000 + (Math.random() * 20000));
                waitMillis(timeMillis);
                log.info("Ошибка получения данных из ФГИС Аршин. Следующая попытка через {} сек", timeMillis/1000 );
                return getVerificationItems(verificationRequest, failCount);
            } else {
                throw new HttpServerErrorException(ex.getStatusCode(), ex.getMessage());
            }
        }
    }


    public static MitItem getMiTypeItemIfOnlyMatches(String miTypeRequest, int failCount) {
        try {
            RestClient restClient = RestClient.create();
            MitResponse response = restClient.get().uri(miTypeRequest).retrieve().body(MitResponse.class);
            if (response != null && response.getResult().getCount() == 1) {
                return response.getResult().getItems().get(0);
             }
            throw new ArshinClientResponseException(getErrorMessage(response));
        } catch (ResourceAccessException ex){
            throw new ArshinClientResponseException("Ошибка соединения с сервером ФГИС\"Аршин\". Проверьте интернет соединение и доступность серверов ФГИС \"Аршин\"");
        } catch (HttpServerErrorException ex) {
            if (failCount < FAIL_COUNT_LIMIT) {
                failCount++;
                return getMiTypeItemIfOnlyMatches(miTypeRequest, failCount);
            } else {
                throw new HttpServerErrorException(ex.getStatusCode(), ex.getMessage());
            }
        }
    }

    private static String getErrorMessage(Response response){
        if (response == null){
            return "Сервер ФГИС \"Аршин\" не отвечает";
        }
        if (response.getResult().getCount() == 0){
            return "По указанному запросу записей в ФГИС \"Аршин\" не найдено";
        }
        return "Ошибка чтения данных, полученных от ФГИС \"Аршин\"";
    }



}
