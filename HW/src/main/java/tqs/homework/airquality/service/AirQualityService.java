package tqs.homework.airquality.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tqs.homework.airquality.cache.Cache;
import tqs.homework.airquality.model.AirMetrics;
import tqs.homework.airquality.repository.WeatherBitRepository;

@Service
public class AirQualityService {

    //private final Cache cache = new Cache(5 * 60L);
    private final Cache cache = new Cache(10);



    @Autowired
    private WeatherBitRepository weatherBitRepository;

    public AirMetrics getCurrentAirMetrics(long cityId) {
        String cityIdString = String.valueOf(cityId);
        AirMetrics result = cache.getRequest(cityIdString);
        if (result == null) {
            result = this.weatherBitRepository.getMetrics(cityId);
                if (result == null) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "Request not valid.  Please check api token and city_id");
                }
        }
        cache.storeRequest(cityIdString, result);
        return result;
    }
    public Cache getCacheStatistics() {
        return cache;
    }
}
