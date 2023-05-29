package me.puugz.meetup.store;

import com.mongodb.MongoCommandException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import me.puugz.meetup.UHCMeetup;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author puugz
 * @since May 29, 2023
 */
public class MongoRepository<T> {

    private static final UpdateOptions UPDATE_OPTIONS = new UpdateOptions().upsert(true);

    private final Class<T> type;
    private final MongoCollection<Document> collection;

    public MongoRepository(String id, Class<T> type, MongoDatabase database) {
        this.type = type;

        try {
            database.createCollection(id);
        } catch (MongoCommandException ignored) {}
        this.collection = database.getCollection(id);
    }

    public T find(UUID key) {
        return this.find(Filters.eq(key.toString()));
    }

    public T find(Bson filter) {
        final Document document = this.collection.find(filter).first();
        if (document != null)
            return UHCMeetup.GSON.fromJson(document.toJson(), type);
        return null;
    }

    public CompletableFuture<T> findAsync(UUID key) {
        return CompletableFuture.supplyAsync(() -> this.find(key));
    }

    public CompletableFuture<T> findAsync(Bson filter) {
        return CompletableFuture.supplyAsync(() -> this.find(filter));
    }

    public List<T> findAll() {
        return this.findAll(null);
    }

    public List<T> findAll(Bson filter) {
        final List<T> list = new ArrayList<>();
        FindIterable<Document> all;

        if (filter != null)
            all = this.collection.find(filter);
        else
            all = this.collection.find();

        all.forEach((Consumer<? super Document>) it ->
                list.add(UHCMeetup.GSON.fromJson(it.toJson(), type)));

        return list;
    }

    public CompletableFuture<List<T>> findAllAsync(Bson filter) {
        return CompletableFuture.supplyAsync(() -> this.findAll(filter));
    }

    public void store(UUID key, T value) {
        this.store(Filters.eq(key.toString()), value);
    }

    public void store(Bson filter, T value) {
        this.collection.updateOne(
                filter,
                new Document("$set", Document.parse(UHCMeetup.GSON.toJson(value, type))),
                UPDATE_OPTIONS
        );
    }

    public CompletableFuture<Void> storeAsync(UUID key, T value) {
        return CompletableFuture.supplyAsync(() -> {
            this.store(key, value);
            return null;
        });
    }

    public CompletableFuture<Void> storeAsync(Bson filter, T value) {
        return CompletableFuture.supplyAsync(() -> {
            this.store(filter, value);
            return null;
        });
    }

    public void delete(UUID key) {
        this.collection.deleteOne(Filters.eq(key.toString()));
    }

    public CompletableFuture<Void> deleteAsync(UUID key) {
        return CompletableFuture.supplyAsync(() -> {
            this.delete(key);
            return null;
        });
    }

    public CompletableFuture<DeleteResult> deleteAllAsync() {
        return CompletableFuture.supplyAsync(() -> this.collection.deleteMany(new Document()));
    }
}
