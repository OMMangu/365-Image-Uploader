package com.ampmangu.imageuploader.utils;


public class BucketResponse {
    public static class Builder {
        private static final long serialVersionUID = 214L;
        private int code;
        private String response;
        private String path;

        public Builder(String response) {
            this.response = response;
        }

        public Builder withCode(int code) {
            this.code = code;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public BucketResponse build() {
            BucketResponse bucketResponse = new BucketResponse();
            bucketResponse.setResponse(this.response);
            bucketResponse.setCode(this.code);
            bucketResponse.setPath(this.path);
            return bucketResponse;
        }


    }

    private static final long serialVersionUID = 214L;
    private int code;
    private String response;
    private String path;

    private BucketResponse() {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
