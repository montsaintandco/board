package com.coco.board.service.posts;

import com.coco.board.domain.posts.Posts;
import com.coco.board.domain.posts.PostsRepository;
import com.coco.board.web.dto.PostsRequestDto;
import com.coco.board.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    /* CREATE */
    @Transactional
    public Long write(PostsRequestDto dto) {

        Posts posts = dto.toEntity();

        postsRepository.save(posts);

        return posts.getId();
    }

    /* READ 게시글 리스트 조회 readOnly 속성으로 조회속도 개선 */
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        return new PostsResponseDto(posts);
    }

    /* UPDATE (dirty checking)*/
    @Transactional
    public Long update(Long id, PostsRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    /* DELETE */
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        postsRepository.delete(posts);
    }

    /* Views Counting */
    @Transactional
    public int updateView(Long id) {
        return postsRepository.updateView(id);
    }


    /* Paging and Sort */
    @Transactional(readOnly = true)
    public Page<Posts> pageList(Pageable pageable) {
        return postsRepository.findAll(pageable);
    }

    /* search */
    @Transactional
    public Page<Posts> search(String keyword, Pageable pageable) {
        Page<Posts> postsList = postsRepository.findByTitleContaining(keyword, pageable);

        return postsList;
    }
}

