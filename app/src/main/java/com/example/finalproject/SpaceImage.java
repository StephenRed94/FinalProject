package com.example.finalproject;

public class SpaceImage {
        public long id;
        public String imgUrl;
        public SpaceImage(long id, String imgUrl){
            this.id = id;
            this.imgUrl = imgUrl;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

