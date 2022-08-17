package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.example.JAM.Article;
import com.KoreaIT.example.JAM.container.Container;
import com.KoreaIT.example.JAM.service.ArticleService;
import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class ArticleController extends Controller {

	private ArticleService articleService;

	public ArticleController() {
		articleService = Container.articleService;
	}

	public void doWrite(String cmd) {
		System.out.println("== 게시물 작성 ==");
		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();

		int id = articleService.doWrite(title, body);

		System.out.printf("%d번 게시물이 생성되었습니다.\n", id);
	}

	public void showList(String cmd) {
		System.out.println("== 게시물 목록 ==");

		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("ORDER BY id DESC");

		List<Map<String, Object>> articlesListMap = DBUtil.selectRows(Container.conn, sql);

		for (Map<String, Object> articleMap : articlesListMap) {
			articles.add(new Article(articleMap));
		}

		if (articles.size() == 0) {
			System.out.println("게시물이 없습니다");
			return;
		}
		System.out.println("번호  /  제목");

		for (Article article : articles) {
			System.out.printf("%d  /  %s\n", article.id, article.title);
		}
	}

	public void doModify(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		System.out.printf("== %d번 게시물 수정 ==\n", id);

		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append(" WHERE id = ?", id);

		int articlesCount = DBUtil.selectRowIntValue(Container.conn, sql);

		if (articlesCount == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}

		System.out.printf("새 제목 : ");
		String title = sc.nextLine();
		System.out.printf("새 내용 : ");
		String body = sc.nextLine();

		sql = new SecSql();
		sql.append("UPDATE article");
		sql.append(" SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);
		sql.append(" WHERE id = ?", id);

		DBUtil.update(Container.conn, sql);

		System.out.printf("%d번 게시물이 수정되었습니다.\n", id);
	}

	public void doDelete(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		System.out.println("== 게시물 삭제 ==");
		
		boolean isArticleExists = articleService.isArticleExists(id);

		if (isArticleExists == false) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}

		articleService.doDelete(id);
		
		System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
	}

	public void showDetail(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append(" WHERE id = ?", id);

		Map<String, Object> articleMap = DBUtil.selectRow(Container.conn, sql);

		if (articleMap.isEmpty()) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}

		System.out.printf("== %d번 게시물 상세보기 ==\n", id);

		Article article = new Article(articleMap);

		System.out.printf("번호 : %d\n", article.id);
		System.out.printf("작성날짜 : %s\n", article.regDate);
		System.out.printf("수정날짜 : %s\n", article.updateDate);
		System.out.printf("제목 : %s\n", article.title);
		System.out.printf("내용 : %s\n", article.body);
	}

}
