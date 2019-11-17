package com.example.postsapi.repository;

import com.example.postsapi.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}
