// sseService.js
export default class SSEService {
    constructor(url) {
      this.url = url;
    }
  
    connect(onMessage) {
      this.eventSource = new EventSource(this.url);
      this.eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        onMessage(data);
      };
      this.eventSource.onerror = (error) => {
        console.error("SSE Error:", error);
        this.disconnect();
      };
    }
  
    disconnect() {
      if (this.eventSource) {
        this.eventSource.close();
      }
    }
  }
  