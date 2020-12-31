import axios from 'axios';

const baseUrl = 'http://localhost:18080';

export const deletePhonebook = (pid) => axios.delete(baseUrl + `/phonebook/${pid}`);

export const createPhonebook = () => axios.post(baseUrl + '/phonebooks');

export const getContactsInPhonebook = (pid) => axios.get(baseUrl + `/phonebook/${pid}/contacts`);

export const getContactById = (pid, cid) => axios.get(baseUrl + `/phonebook/${pid}/contact/${cid}`);

export const createContact = (pid, contact) => axios.post(baseUrl + `/phonebook/${pid}/contacts`, contact);

export const updateContact = (pid, cid, contact) => axios.put(baseUrl + `/phonebook/${pid}/contact/${cid}`, contact);

export const deleteContact = (pid, cid) => axios.delete(baseUrl + `/phonebook/${pid}/contact/${cid}`);
