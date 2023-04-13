import { request } from "../../base/HTTP";
import HttpMethod from "../../constants/HttpMethod";

export async function getCerts(data) {
    return await request('/api/cert/', data);
}

export async function addCert(data) {
    return await request('/api/pk', data, HttpMethod.POST);
}

export async function deleteCert(data) {
    return await request('/api/cert/' +  data.id, {}, HttpMethod.DELETE);
}