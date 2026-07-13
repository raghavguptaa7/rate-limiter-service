import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 100,
    duration: '30s',
};

export default function () {

    const payload = JSON.stringify({
        clientId: "raghav"
    });

    const params = {
        headers: {
            "Content-Type": "application/json",
            "X-API-KEY": "raghav-secret-key"
        }
    };

    const res = http.post(
        "http://localhost:8080/api/allow",
        payload,
        params
    );

    check(res, {
        "status is 200": (r) => r.status === 200,
    });

    sleep(1);
}