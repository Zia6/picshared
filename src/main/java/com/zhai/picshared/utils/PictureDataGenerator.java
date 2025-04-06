//package com.zhai.picshared.utils;
//
//import com.zhai.picshared.model.entity.Picture;
//import com.zhai.picshared.service.PictureService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Date;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class PictureDataGenerator implements CommandLineRunner {
//
//    private final PictureService pictureService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        int total = 1_000_000; // 总条数
//        int batchSize = 1000;  // 每批插入条数
//        List<Picture> batchList = new ArrayList<>(batchSize);
//
//        log.info("开始批量生成 Picture 数据...");
//
//        long start = System.currentTimeMillis();
//
//        for (int i = 1; i <= total; i++) {
//            Picture picture = new Picture();
//            picture.setUrl("https://dummy.com/" + i);
//            picture.setName("TestPic_" + i);
//            picture.setIntroduction("批量生成的简介_" + i);
//            picture.setCategory("测试分类");
//            picture.setTags("[\"标签1\", \"标签2\"]");
//            picture.setPicSize(1024L);
//            picture.setPicWidth(800);
//            picture.setPicHeight(600);
//            picture.setPicScale(1.33);
//            picture.setPicFormat("jpg");
//            picture.setUserId(1L);
//            picture.setCreateTime(new Date());
//            picture.setEditTime(new Date());
//            picture.setIsDelete(0);
//            picture.setReviewStatus(1);
//            picture.setThumbnailUrl("https://dummy.com/thumb_" + i);
//            batchList.add(picture);
//
//            // 每批插入
//            if (batchList.size() >= batchSize) {
//                pictureService.saveBatch(batchList);
//                batchList.clear();
//                log.info("已插入 {} 条", i);
//            }
//        }
//
//        // 剩余不足一批的
//        if (!batchList.isEmpty()) {
//            pictureService.saveBatch(batchList);
//            log.info("已插入 {} 条", total);
//        }
//
//        long end = System.currentTimeMillis();
//        log.info("批量生成完成，总耗时: {} 秒", (end - start) / 1000);
//    }
//}
