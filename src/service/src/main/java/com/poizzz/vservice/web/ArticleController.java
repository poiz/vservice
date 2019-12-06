package com.poizzz.vservice.web;

import com.poizzz.vservice.dto.ArticleDto;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @GetMapping("/list")
    public ResponseEntity<List<ArticleDto>> list() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> read(@PathVariable Long id) {
        ArticleDto article = new ArticleDto();
        article.setAuthor("author");
        article.setContent("content");
        article.setCreated(new Date());
        article.setHeadImgUrl("url");
        article.setTitle("title");
        if (SecurityUtils.getSubject().getPrincipal() != null) {
            article.setIssueTime(new Date());
            article.setStatus(1);
            article.setModified(new Date());
            article.setCreateUserId(1L);
            article.setCreateUserName("creator");
        }
        return ResponseEntity.ok(article);
    }
}
