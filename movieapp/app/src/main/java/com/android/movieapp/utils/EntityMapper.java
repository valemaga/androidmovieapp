package com.android.movieapp.utils;

public interface EntityMapper<Entity, DomainModel> {

    public DomainModel mapFromEntity(Entity entity);

    public Entity mapToEntity(DomainModel domainModel);

}


