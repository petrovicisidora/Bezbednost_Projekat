import { request } from "../../base/HTTP";
import HttpMethod from "../../constants/HttpMethod";

export async function getAlarms(data) {
    return await request('/api/alarm/', data);
}