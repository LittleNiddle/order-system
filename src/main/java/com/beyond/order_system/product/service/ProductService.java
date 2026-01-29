package com.beyond.order_system.product.service;

import com.beyond.order_system.common.config.AwsS3Config;
import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.member.repository.MemberRepository;
import com.beyond.order_system.product.domain.Product;
import com.beyond.order_system.product.dto.ProductCreateReqDto;
import com.beyond.order_system.product.dto.ProductDetailResDto;
import com.beyond.order_system.product.dto.ProductListResDto;
import com.beyond.order_system.product.dto.ProductSearchReqDto;
import com.beyond.order_system.product.repository.ProductRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final S3Client s3Client;
    @Value("${aws.s3.bucket1}")
    private String bucket;

    public ProductService(ProductRepository productRepository, MemberRepository memberRepository, S3Client s3Client) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.s3Client = s3Client;
    }

    public Product create(ProductCreateReqDto dto, MultipartFile productImage, String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new NoSuchElementException("등록된 이메일이 없습니다."));
        Product product = dto.toEntity(member);
        Product productDb = productRepository.save(product);

        if(productImage != null) {
            String filename = "product-" + product.getId() + "-productImage-" + productImage.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(filename)
                    .contentType(productImage.getContentType()) // image/jpeg, video/mp4, ...
                    .build();

            try {
                s3Client.putObject(request, RequestBody.fromBytes(productImage.getBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String imageUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(filename)).toExternalForm();
            product.updateProfileImageUrl(imageUrl);
        }
        return productDb;
    }

    public ProductDetailResDto findById(Long id){
        return ProductDetailResDto.fromEntity(productRepository.findById(id).orElseThrow(()->new NoSuchElementException("해당 상품이 없습니다.")));
    }

    public Page<ProductListResDto> findAll(ProductSearchReqDto searchDto, Pageable pageable){
        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if(searchDto.getName()!=null) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%"+searchDto.getName()+"%"));
                }
                if(searchDto.getCategory()!=null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), searchDto.getCategory()));
                }

                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for (int i = 0; i < predicateArr.length; i++) {
                    predicateArr[i] = predicateList.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };

        Page<Product> productList = productRepository.findAll(specification, pageable);
        return productList.map(ProductListResDto::fromEntity);
    }
}
