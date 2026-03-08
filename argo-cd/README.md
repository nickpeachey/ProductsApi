# Argo CD Setup for Next.js and .NET Web API

This directory contains the Kubernetes and Argo CD configuration to deploy a Next.js application and a .NET Web API to an Azure Kubernetes Service (AKS) cluster.

## Directory Structure

- `nextjs-app/`: Contains the Kubernetes manifests for the Next.js application.
  - `deployment.yaml`: Defines the deployment of the Next.js application.
  - `service.yaml`: Exposes the Next.js application within the cluster.
  - `ingress.yaml`: Exposes the Next.js application to the internet via an Azure Application Gateway.
- `dotnet-api/`: Contains the Kubernetes manifests for the .NET Web API.
  - `deployment.yaml`: Defines the deployment of the .NET Web API.
  - `service.yaml`: Exposes the .NET Web API within the cluster.
  - `ingress.yaml`: Exposes the .NET Web API to the internet via an Azure Application Gateway.
- `kustomization.yaml`: The Kustomize file to manage all the Kubernetes resources.
- `argo-cd-application.yaml`: The Argo CD Application manifest.

## Prerequisites

1.  An Azure account with an active subscription.
2.  An Azure Kubernetes Service (AKS) cluster.
3.  `kubectl` installed and configured to connect to your AKS cluster.
4.  Argo CD installed in your AKS cluster. If you haven't installed it, you can do so by running the following commands:

    ```bash
    kubectl create namespace argocd
    kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
    ```

## How to Use

1.  **Update the placeholders:**
    -   In `argo-cd/nextjs-app/deployment.yaml`, replace `<your-nextjs-app-image>` with the Docker image of your Next.js application.
    -   In `argo-cd/nextjs-app/ingress.yaml`, replace `<your-nextjs-app-hostname>` with the hostname you want to use for your Next.js application.
    -   In `argo-cd/dotnet-api/deployment.yaml`, replace `<your-dotnet-api-image>` with the Docker image of your .NET Web API.
    -   In `argo-cd/dotnet-api/ingress.yaml`, replace `<your-dotnet-api-hostname>` with the hostname you want to use for your .NET Web API.
    -   In `argo-cd/argo-cd-application.yaml`, replace `<your-git-repo-url>` with the URL of the Git repository where you will store these configuration files.

2.  **Commit and push the changes:**
    Commit all the files in this directory to your Git repository.

3.  **Apply the Argo CD Application manifest:**
    Apply the `argo-cd-application.yaml` manifest to your cluster:

    ```bash
    kubectl apply -f argo-cd/argo-cd-application.yaml
    ```

4.  **Access the Argo CD UI:**
    You can access the Argo CD UI by running the following command:

    ```bash
    kubectl port-forward svc/argocd-server -n argocd 8080:443
    ```

    Then, open your browser and navigate to `https://localhost:8080`.

    The initial password for the `admin` user can be retrieved with the following command:

    ```bash
    kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
    ```

Once you have logged in, you should see your `my-apps` application in the Argo CD UI. Argo CD will automatically sync the application, and your Next.js and .NET Web API applications will be deployed to your AKS cluster.
