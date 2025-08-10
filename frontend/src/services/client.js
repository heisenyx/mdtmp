import axios from 'axios';

export const savePublication = async (title, author, content, ttlMinutes) => {
  return await axios.post(
    `/api/publications/publish`,
    { title, author, content, ttlMinutes }
  );
}

export const enhancePublication = async (title, content) => {
  return await axios.post(
    `/api/publications/enhance`,
    { title, content }
  );
}

export const getPublication = async (hash) => {
  return await axios.get(
    `/api/publications/${hash}`
  );
}