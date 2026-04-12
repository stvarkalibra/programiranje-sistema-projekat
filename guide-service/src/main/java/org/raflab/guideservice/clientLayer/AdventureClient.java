package org.raflab.guideservice.clientLayer;

import org.raflab.guideservice.config.FeignRequestInterceptorConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "avantura-service",
        url = "http://localhost:8081/api/adventures",
        configuration = FeignRequestInterceptorConfig.class
)
public interface AdventureClient {

    @GetMapping("/guide/{guideId}/premium")
    List<RemoteAdventureDTO> getPremiumAdventures(
            @PathVariable("guideId") Long guideId,
            @RequestParam("minPrice") Double minPrice);

    @GetMapping("/guide/{guideId}/revenue")
    Double getGuideRevenue(@PathVariable("guideId") Long guideId);

    @GetMapping("/guide/{guideId}/check-overload")
    Boolean isGuideOverloaded(@PathVariable("guideId") Long guideId);

    @GetMapping("/category/{categoryId}")
    List<RemoteAdventureDTO> getByCategory(@PathVariable("categoryId") Long categoryId);
}