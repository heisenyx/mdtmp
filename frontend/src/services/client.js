import axios from 'axios';

export const savePublication = async (title, author, content, ttlMinutes) => {
  try {
    return await axios.post(
      `/api/publications/publish`,
      { title, author, content, ttlMinutes }
    );
  } catch (e) {
    throw e;
  }
}

export const enhancePublication = async (title, content) => {
  try {
    return await axios.post(
      `/api/publications/enhance`,
      { title, content }
    );
  } catch (e) {
    throw e;
  }
}

export const getPublication = async (hash) => {
  return await axios.get(
    `/api/publications/${hash}`
  );
}