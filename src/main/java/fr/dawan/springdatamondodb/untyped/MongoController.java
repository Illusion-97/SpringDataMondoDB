package fr.dawan.springdatamondodb.untyped;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static fr.dawan.springdatamondodb.untyped.helper.QueryBuilder.build;

@RestController
@RequestMapping("api/{collection}")
@RequiredArgsConstructor
public class MongoController {

    private final MongoTemplate template;

    @GetMapping
    public List<Document> find(@PathVariable String collection, @RequestParam Map<String, String> params, Pageable pageable) {
        return template.find(build(params, pageable), Document.class, collection);
    }

    @PostMapping
    public Document save(@PathVariable String collection, @RequestBody Document document) {
        return template.save(document,collection);
    }

    @PutMapping
    public Document upsert(@PathVariable String collection, @RequestBody Map<String,Document> document, @RequestParam Map<String, String> params, Pageable pageable) {
        Query build = build(params, pageable);
        Update update = Update.fromDocument(new Document(document));
        FindAndModifyOptions upsert = new FindAndModifyOptions().returnNew(true);
        return template.findAndModify(build, update, upsert, Document.class, collection);
    }
    @DeleteMapping
    public List<Document> delete(@PathVariable String collection, @RequestParam Map<String, String> params, Pageable pageable) {
        return template.findAllAndRemove(build(params, pageable), Document.class, collection);
    }
}
