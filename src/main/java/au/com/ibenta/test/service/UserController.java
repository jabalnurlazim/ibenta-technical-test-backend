package au.com.ibenta.test.service;

import au.com.ibenta.test.model.UserParams;
import au.com.ibenta.test.response.ResponseBase;
import io.swagger.annotations.Api;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.HashMap;

@Api(tags = "users")
@RestController
@RequestMapping(path="/users")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping(path="/create")
    public @ResponseBody
    ResponseEntity<String> create (@RequestBody UserParams user) {
       return userService.create(user);
    }

    @GetMapping(path="/get/{userId}")
    public @ResponseBody
    ResponseEntity<String> get(@PathVariable Long userId) { return userService.get(userId); }

    @PutMapping(path="/update/{userId}")
    public @ResponseBody
    ResponseEntity<String> update (@RequestBody UserParams userParams,
                                   @PathVariable Long userId) {
        return userService.update(userParams, userId);
    }

    @DeleteMapping(path="/delete/{userId}")
    public @ResponseBody
    ResponseEntity<String> delete (@PathVariable Long userId) {
        return userService.delete(userId);
    }

    @GetMapping(path="/list")
    public @ResponseBody
    ResponseEntity<String> list() {  return userService.list(); }

    @GetMapping(path = "/checkHealth")
    public @ResponseBody
    ResponseBase checkHealth() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://authentication-service-jx-staging.gitops.ibenta.com/actuator/health";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, 1);
        if(response.getStatusCode() == HttpStatus.OK) {
            return ResponseBase.newSuccessResponse(ResponseBase.OK, response.getBody());
        }else {
            return ResponseBase.newSuccessResponse(ResponseBase.EMPTY, null);
        }
    }

    @GetMapping(path = "/checkMacauLottery")
    public @ResponseBody
    String checkMacauLottery() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://wwwamlhc2.com/api/HistoryOpenInfo";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap<String, String> params = new HashMap<>();
        params.put("lotteryId", "2032");
        params.put("issueNum", "1");
        params.put("limit", "10");
        HttpEntity<String> requestEntity = new HttpEntity<>(new JSONObject(params).toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, 1);

        return response.getBody();
    }
}