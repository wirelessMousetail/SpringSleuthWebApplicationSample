package org.mousetail.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sample controller to demonstrate an issue, we're faced with:
 * spans for error responses (4xx and 5xx) are never closed,
 * if they returns with ResponseEntity object.
 */
@RestController
public class SampleController {

    /**
     * Span for this request will be processed and closed correctly.
     * @return
     */
    @RequestMapping(path = "/test_ok", method = RequestMethod.GET)
    public ResponseEntity<?> processOk() {
        return ResponseEntity.ok("test");
    }

    /**
     * Span for this request will be detached and never closed.
     * @return
     */
    @RequestMapping(path = "/test_bad_request", method = RequestMethod.GET)
    public ResponseEntity<?> processFail() {
        return ResponseEntity.badRequest().build();
    }
}
