package com.example.demo.controller;
import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;
    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String category){
        if (category != null && !category.isEmpty()) {
            return Result.success(bookService.listByCategory(category));
        }
        return Result.success(bookService.list());
    }
    @GetMapping ("/{id}")
    public Result getById(@PathVariable Integer id) {
        Book book = bookService.getById(id);
        if (book == null) {
            return Result.error("图书不存在");
        }
        return Result.success(book);
    }
    @PostMapping
    public Result add(@RequestBody Book book){
        return Result.success(bookService.add(book));
    }
    @PutMapping
    public Result update(@RequestBody Book book){
        return Result.success(bookService.update(book));
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(bookService.delete(id));
    }


}