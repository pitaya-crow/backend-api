package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookService {

            private final BookMapper bookMapper;

            public BookService(BookMapper bookMapper) {
                this.bookMapper = bookMapper;
            }

            /** 图书列表 */
            public List<Book> list() {
                return bookMapper.findALL();
            }

            /** 按分类查询图书 */
            public List<Book> listByCategory(String category) {
                return bookMapper.findByCategory(category);
            }

            /** 图书详情 */
            public Book getById(Integer id) {
                if (id == null) {
                    return null;
                }
                return bookMapper.findById(id);
            }

            /** 新增图书 */
            public Book add(Book book) {
                if (book == null) {
                    throw new IllegalArgumentException("图书信息不能为空");
                }
                if (book.getBorrowCount() == null) {
                    book.setBorrowCount(0);
                }
                if (book.getAvailable() == null && book.getTotal() != null) {
                    book.setAvailable(book.getTotal());
                }
                if (book.getCreateTime() == null) {
                    book.setCreateTime(LocalDateTime.now());
                }
                // rating 由书评汇总，新增时不设置

                bookMapper.insert(book);
                return book;
            }

            /** 修改图书（只更新前端传了的字段） */
            public Book update(Book incoming) {
                if (incoming == null || incoming.getId() == null) {
                    throw new IllegalArgumentException("图书 ID 不能为空");
                }

                Book existing = bookMapper.findById(incoming.getId().intValue());
                if (existing == null) {
                    throw new IllegalArgumentException("图书不存在");
                }

                if (incoming.getTitle() != null) existing.setTitle(incoming.getTitle());
                if (incoming.getAuthor() != null) existing.setAuthor(incoming.getAuthor());
                if (incoming.getIsbn() != null) existing.setIsbn(incoming.getIsbn());
                if (incoming.getTotal() != null) existing.setTotal(incoming.getTotal());
                if (incoming.getAvailable() != null) existing.setAvailable(incoming.getAvailable());
                if (incoming.getCreateTime() != null) existing.setCreateTime(incoming.getCreateTime());
                if (incoming.getCategory() != null) existing.setCategory(incoming.getCategory());
                if (incoming.getPublisher() != null) existing.setPublisher(incoming.getPublisher());
                if (incoming.getPublishDate() != null) existing.setPublishDate(incoming.getPublishDate());
                if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
                if (incoming.getCoverUrl() != null) existing.setCoverUrl(incoming.getCoverUrl());

                bookMapper.update(existing);
                return existing;
            }

            /** 删除图书 */
            public boolean delete(Integer id) {
                if (id == null) {
                    return false;
                }
                return bookMapper.deleteById(id) > 0;
            }
        }


