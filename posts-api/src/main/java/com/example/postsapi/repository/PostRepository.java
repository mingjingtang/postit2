package com.example.postsapi.repository;

import com.example.postsapi.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

}
